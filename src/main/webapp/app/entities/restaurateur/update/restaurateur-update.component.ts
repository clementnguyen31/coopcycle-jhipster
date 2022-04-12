import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRestaurateur, Restaurateur } from '../restaurateur.model';
import { RestaurateurService } from '../service/restaurateur.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';

@Component({
  selector: 'jhi-restaurateur-update',
  templateUrl: './restaurateur-update.component.html',
})
export class RestaurateurUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClient[] = [];
  commandesSharedCollection: ICommande[] = [];

  editForm = this.fb.group({
    id: [],
    restaurateurId: [],
    restaurateurNom: [],
    restaurateurPrenom: [],
    restaurateurBoutique: [],
    clients: [],
    commande: [],
  });

  constructor(
    protected restaurateurService: RestaurateurService,
    protected clientService: ClientService,
    protected commandeService: CommandeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurateur }) => {
      this.updateForm(restaurateur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurateur = this.createFromForm();
    if (restaurateur.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurateurService.update(restaurateur));
    } else {
      this.subscribeToSaveResponse(this.restaurateurService.create(restaurateur));
    }
  }

  trackClientById(_index: number, item: IClient): number {
    return item.id!;
  }

  trackCommandeById(_index: number, item: ICommande): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurateur>>): void {
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

  protected updateForm(restaurateur: IRestaurateur): void {
    this.editForm.patchValue({
      id: restaurateur.id,
      restaurateurId: restaurateur.restaurateurId,
      restaurateurNom: restaurateur.restaurateurNom,
      restaurateurPrenom: restaurateur.restaurateurPrenom,
      restaurateurBoutique: restaurateur.restaurateurBoutique,
      clients: restaurateur.clients,
      commande: restaurateur.commande,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(
      this.clientsSharedCollection,
      ...(restaurateur.clients ?? [])
    );
    this.commandesSharedCollection = this.commandeService.addCommandeToCollectionIfMissing(
      this.commandesSharedCollection,
      restaurateur.commande
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(
        map((clients: IClient[]) =>
          this.clientService.addClientToCollectionIfMissing(clients, ...(this.editForm.get('clients')!.value ?? []))
        )
      )
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    this.commandeService
      .query()
      .pipe(map((res: HttpResponse<ICommande[]>) => res.body ?? []))
      .pipe(
        map((commandes: ICommande[]) =>
          this.commandeService.addCommandeToCollectionIfMissing(commandes, this.editForm.get('commande')!.value)
        )
      )
      .subscribe((commandes: ICommande[]) => (this.commandesSharedCollection = commandes));
  }

  protected createFromForm(): IRestaurateur {
    return {
      ...new Restaurateur(),
      id: this.editForm.get(['id'])!.value,
      restaurateurId: this.editForm.get(['restaurateurId'])!.value,
      restaurateurNom: this.editForm.get(['restaurateurNom'])!.value,
      restaurateurPrenom: this.editForm.get(['restaurateurPrenom'])!.value,
      restaurateurBoutique: this.editForm.get(['restaurateurBoutique'])!.value,
      clients: this.editForm.get(['clients'])!.value,
      commande: this.editForm.get(['commande'])!.value,
    };
  }
}
