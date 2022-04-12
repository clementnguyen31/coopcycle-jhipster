import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICooperative, Cooperative } from '../cooperative.model';
import { CooperativeService } from '../service/cooperative.service';
import { ILivreur } from 'app/entities/livreur/livreur.model';
import { LivreurService } from 'app/entities/livreur/service/livreur.service';
import { IRestaurateur } from 'app/entities/restaurateur/restaurateur.model';
import { RestaurateurService } from 'app/entities/restaurateur/service/restaurateur.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-cooperative-update',
  templateUrl: './cooperative-update.component.html',
})
export class CooperativeUpdateComponent implements OnInit {
  isSaving = false;

  livreursSharedCollection: ILivreur[] = [];
  restaurateursSharedCollection: IRestaurateur[] = [];
  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    cooperativeId: [],
    cooperativeNom: [],
    cooperativeDirecteur: [],
    cooperativeVille: [],
    cooperativeMetropole: [],
    cooperativeCommune: [],
    livreurs: [],
    restaurateurs: [],
    clients: [],
  });

  constructor(
    protected cooperativeService: CooperativeService,
    protected livreurService: LivreurService,
    protected restaurateurService: RestaurateurService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cooperative }) => {
      this.updateForm(cooperative);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cooperative = this.createFromForm();
    if (cooperative.id !== undefined) {
      this.subscribeToSaveResponse(this.cooperativeService.update(cooperative));
    } else {
      this.subscribeToSaveResponse(this.cooperativeService.create(cooperative));
    }
  }

  trackLivreurById(_index: number, item: ILivreur): number {
    return item.id!;
  }

  trackRestaurateurById(_index: number, item: IRestaurateur): number {
    return item.id!;
  }

  trackClientById(_index: number, item: IClient): number {
    return item.id!;
  }

  getSelectedLivreur(option: ILivreur, selectedVals?: ILivreur[]): ILivreur {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedRestaurateur(option: IRestaurateur, selectedVals?: IRestaurateur[]): IRestaurateur {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedClient(option: IClient, selectedVals?: IClient[]): IClient {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICooperative>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cooperative: ICooperative): void {
    this.editForm.patchValue({
      id: cooperative.id,
      cooperativeId: cooperative.cooperativeId,
      cooperativeNom: cooperative.cooperativeNom,
      cooperativeDirecteur: cooperative.cooperativeDirecteur,
      cooperativeVille: cooperative.cooperativeVille,
      cooperativeMetropole: cooperative.cooperativeMetropole,
      cooperativeCommune: cooperative.cooperativeCommune,
      livreurs: cooperative.livreurs,
      restaurateurs: cooperative.restaurateurs,
      clients: cooperative.clients,
    });

    this.livreursSharedCollection = this.livreurService.addLivreurToCollectionIfMissing(
      this.livreursSharedCollection,
      ...(cooperative.livreurs ?? [])
    );
    this.restaurateursSharedCollection = this.restaurateurService.addRestaurateurToCollectionIfMissing(
      this.restaurateursSharedCollection,
      ...(cooperative.restaurateurs ?? [])
    );
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(
      this.clientsSharedCollection,
      ...(cooperative.clients ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.livreurService
      .query()
      .pipe(map((res: HttpResponse<ILivreur[]>) => res.body ?? []))
      .pipe(
        map((livreurs: ILivreur[]) =>
          this.livreurService.addLivreurToCollectionIfMissing(livreurs, ...(this.editForm.get('livreurs')!.value ?? []))
        )
      )
      .subscribe((livreurs: ILivreur[]) => (this.livreursSharedCollection = livreurs));

    this.restaurateurService
      .query()
      .pipe(map((res: HttpResponse<IRestaurateur[]>) => res.body ?? []))
      .pipe(
        map((restaurateurs: IRestaurateur[]) =>
          this.restaurateurService.addRestaurateurToCollectionIfMissing(restaurateurs, ...(this.editForm.get('restaurateurs')!.value ?? []))
        )
      )
      .subscribe((restaurateurs: IRestaurateur[]) => (this.restaurateursSharedCollection = restaurateurs));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(
        map((clients: IClient[]) =>
          this.clientService.addClientToCollectionIfMissing(clients, ...(this.editForm.get('clients')!.value ?? []))
        )
      )
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): ICooperative {
    return {
      ...new Cooperative(),
      id: this.editForm.get(['id'])!.value,
      cooperativeId: this.editForm.get(['cooperativeId'])!.value,
      cooperativeNom: this.editForm.get(['cooperativeNom'])!.value,
      cooperativeDirecteur: this.editForm.get(['cooperativeDirecteur'])!.value,
      cooperativeVille: this.editForm.get(['cooperativeVille'])!.value,
      cooperativeMetropole: this.editForm.get(['cooperativeMetropole'])!.value,
      cooperativeCommune: this.editForm.get(['cooperativeCommune'])!.value,
      livreurs: this.editForm.get(['livreurs'])!.value,
      restaurateurs: this.editForm.get(['restaurateurs'])!.value,
      clients: this.editForm.get(['clients'])!.value,
    };
  }
}
