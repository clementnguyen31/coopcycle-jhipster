import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CooperativeService } from '../service/cooperative.service';
import { ICooperative, Cooperative } from '../cooperative.model';
import { ILivreur } from 'app/entities/livreur/livreur.model';
import { LivreurService } from 'app/entities/livreur/service/livreur.service';
import { IRestaurateur } from 'app/entities/restaurateur/restaurateur.model';
import { RestaurateurService } from 'app/entities/restaurateur/service/restaurateur.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { CooperativeUpdateComponent } from './cooperative-update.component';

describe('Cooperative Management Update Component', () => {
  let comp: CooperativeUpdateComponent;
  let fixture: ComponentFixture<CooperativeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cooperativeService: CooperativeService;
  let livreurService: LivreurService;
  let restaurateurService: RestaurateurService;
  let clientService: ClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CooperativeUpdateComponent],
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
      .overrideTemplate(CooperativeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CooperativeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cooperativeService = TestBed.inject(CooperativeService);
    livreurService = TestBed.inject(LivreurService);
    restaurateurService = TestBed.inject(RestaurateurService);
    clientService = TestBed.inject(ClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Livreur query and add missing value', () => {
      const cooperative: ICooperative = { id: 456 };
      const livreurs: ILivreur[] = [{ id: 25649 }];
      cooperative.livreurs = livreurs;

      const livreurCollection: ILivreur[] = [{ id: 34940 }];
      jest.spyOn(livreurService, 'query').mockReturnValue(of(new HttpResponse({ body: livreurCollection })));
      const additionalLivreurs = [...livreurs];
      const expectedCollection: ILivreur[] = [...additionalLivreurs, ...livreurCollection];
      jest.spyOn(livreurService, 'addLivreurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cooperative });
      comp.ngOnInit();

      expect(livreurService.query).toHaveBeenCalled();
      expect(livreurService.addLivreurToCollectionIfMissing).toHaveBeenCalledWith(livreurCollection, ...additionalLivreurs);
      expect(comp.livreursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Restaurateur query and add missing value', () => {
      const cooperative: ICooperative = { id: 456 };
      const restaurateurs: IRestaurateur[] = [{ id: 29763 }];
      cooperative.restaurateurs = restaurateurs;

      const restaurateurCollection: IRestaurateur[] = [{ id: 61774 }];
      jest.spyOn(restaurateurService, 'query').mockReturnValue(of(new HttpResponse({ body: restaurateurCollection })));
      const additionalRestaurateurs = [...restaurateurs];
      const expectedCollection: IRestaurateur[] = [...additionalRestaurateurs, ...restaurateurCollection];
      jest.spyOn(restaurateurService, 'addRestaurateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cooperative });
      comp.ngOnInit();

      expect(restaurateurService.query).toHaveBeenCalled();
      expect(restaurateurService.addRestaurateurToCollectionIfMissing).toHaveBeenCalledWith(
        restaurateurCollection,
        ...additionalRestaurateurs
      );
      expect(comp.restaurateursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Client query and add missing value', () => {
      const cooperative: ICooperative = { id: 456 };
      const clients: IClient[] = [{ id: 72498 }];
      cooperative.clients = clients;

      const clientCollection: IClient[] = [{ id: 75618 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [...clients];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cooperative });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cooperative: ICooperative = { id: 456 };
      const livreurs: ILivreur = { id: 94266 };
      cooperative.livreurs = [livreurs];
      const restaurateurs: IRestaurateur = { id: 69132 };
      cooperative.restaurateurs = [restaurateurs];
      const clients: IClient = { id: 7013 };
      cooperative.clients = [clients];

      activatedRoute.data = of({ cooperative });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cooperative));
      expect(comp.livreursSharedCollection).toContain(livreurs);
      expect(comp.restaurateursSharedCollection).toContain(restaurateurs);
      expect(comp.clientsSharedCollection).toContain(clients);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cooperative>>();
      const cooperative = { id: 123 };
      jest.spyOn(cooperativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cooperative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cooperative }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cooperativeService.update).toHaveBeenCalledWith(cooperative);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cooperative>>();
      const cooperative = new Cooperative();
      jest.spyOn(cooperativeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cooperative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cooperative }));
      saveSubject.complete();

      // THEN
      expect(cooperativeService.create).toHaveBeenCalledWith(cooperative);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cooperative>>();
      const cooperative = { id: 123 };
      jest.spyOn(cooperativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cooperative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cooperativeService.update).toHaveBeenCalledWith(cooperative);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLivreurById', () => {
      it('Should return tracked Livreur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLivreurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRestaurateurById', () => {
      it('Should return tracked Restaurateur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRestaurateurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackClientById', () => {
      it('Should return tracked Client primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClientById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedLivreur', () => {
      it('Should return option if no Livreur is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedLivreur(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Livreur for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedLivreur(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Livreur is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedLivreur(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedRestaurateur', () => {
      it('Should return option if no Restaurateur is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedRestaurateur(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Restaurateur for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedRestaurateur(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Restaurateur is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedRestaurateur(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

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
