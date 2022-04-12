import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RestaurateurService } from '../service/restaurateur.service';
import { IRestaurateur, Restaurateur } from '../restaurateur.model';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';

import { RestaurateurUpdateComponent } from './restaurateur-update.component';

describe('Restaurateur Management Update Component', () => {
  let comp: RestaurateurUpdateComponent;
  let fixture: ComponentFixture<RestaurateurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let restaurateurService: RestaurateurService;
  let clientService: ClientService;
  let commandeService: CommandeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RestaurateurUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RestaurateurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurateurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    restaurateurService = TestBed.inject(RestaurateurService);
    clientService = TestBed.inject(ClientService);
    commandeService = TestBed.inject(CommandeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Client query and add missing value', () => {
      const restaurateur: IRestaurateur = { id: 456 };
      const clients: IClient[] = [{ id: 34404 }];
      restaurateur.clients = clients;

      const clientCollection: IClient[] = [{ id: 1167 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [...clients];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ restaurateur });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Commande query and add missing value', () => {
      const restaurateur: IRestaurateur = { id: 456 };
      const commande: ICommande = { id: 18734 };
      restaurateur.commande = commande;

      const commandeCollection: ICommande[] = [{ id: 86572 }];
      jest.spyOn(commandeService, 'query').mockReturnValue(of(new HttpResponse({ body: commandeCollection })));
      const additionalCommandes = [commande];
      const expectedCollection: ICommande[] = [...additionalCommandes, ...commandeCollection];
      jest.spyOn(commandeService, 'addCommandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ restaurateur });
      comp.ngOnInit();

      expect(commandeService.query).toHaveBeenCalled();
      expect(commandeService.addCommandeToCollectionIfMissing).toHaveBeenCalledWith(commandeCollection, ...additionalCommandes);
      expect(comp.commandesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const restaurateur: IRestaurateur = { id: 456 };
      const clients: IClient = { id: 89667 };
      restaurateur.clients = [clients];
      const commande: ICommande = { id: 74038 };
      restaurateur.commande = commande;

      activatedRoute.data = of({ restaurateur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(restaurateur));
      expect(comp.clientsSharedCollection).toContain(clients);
      expect(comp.commandesSharedCollection).toContain(commande);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Restaurateur>>();
      const restaurateur = { id: 123 };
      jest.spyOn(restaurateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurateur }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(restaurateurService.update).toHaveBeenCalledWith(restaurateur);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Restaurateur>>();
      const restaurateur = new Restaurateur();
      jest.spyOn(restaurateurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurateur }));
      saveSubject.complete();

      // THEN
      expect(restaurateurService.create).toHaveBeenCalledWith(restaurateur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Restaurateur>>();
      const restaurateur = { id: 123 };
      jest.spyOn(restaurateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(restaurateurService.update).toHaveBeenCalledWith(restaurateur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClientById', () => {
      it('Should return tracked Client primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClientById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCommandeById', () => {
      it('Should return tracked Commande primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCommandeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedClient', () => {
      it('Should return option if no Client is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedClient(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Client for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedClient(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Client is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedClient(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
