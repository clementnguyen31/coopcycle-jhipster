import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClient, Client } from '../client.model';
import { ClientService } from '../service/client.service';
import { IRecherche } from 'app/entities/recherche/recherche.model';
import { RechercheService } from 'app/entities/recherche/service/recherche.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';

@Component({
  selector: 'jhi-client-update',
  templateUrl: './client-update.component.html',
})
export class ClientUpdateComponent implements OnInit {
  isSaving = false;

  recherchesSharedCollection: IRecherche[] = [];
  commandesSharedCollection: ICommande[] = [];

  editForm = this.fb.group({
    id: [],
    clientId: [],
    clientNom: [],
    clientPrenom: [],
    clientEmail: [],
    clientAdresse: [],
    recherches: [],
    commande: [],
  });

  constructor(
    protected clientService: ClientService,
    protected rechercheService: RechercheService,
    protected commandeService: CommandeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      this.updateForm(client);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const client = this.createFromForm();
    if (client.id !== undefined) {
      this.subscribeToSaveResponse(this.clientService.update(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.create(client));
    }
  }

  trackRechercheById(_index: number, item: IRecherche): number {
    return item.id!;
  }

  trackCommandeById(_index: number, item: ICommande): number {
    return item.id!;
  }

  getSelectedRecherche(option: IRecherche, selectedVals?: IRecherche[]): IRecherche {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>): void {
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

  protected updateForm(client: IClient): void {
    this.editForm.patchValue({
      id: client.id,
      clientId: client.clientId,
      clientNom: client.clientNom,
      clientPrenom: client.clientPrenom,
      clientEmail: client.clientEmail,
      clientAdresse: client.clientAdresse,
      recherches: client.recherches,
      commande: client.commande,
    });

    this.recherchesSharedCollection = this.rechercheService.addRechercheToCollectionIfMissing(
      this.recherchesSharedCollection,
      ...(client.recherches ?? [])
    );
    this.commandesSharedCollection = this.commandeService.addCommandeToCollectionIfMissing(this.commandesSharedCollection, client.commande);
  }

  protected loadRelationshipsOptions(): void {
    this.rechercheService
      .query()
      .pipe(map((res: HttpResponse<IRecherche[]>) => res.body ?? []))
      .pipe(
        map((recherches: IRecherche[]) =>
          this.rechercheService.addRechercheToCollectionIfMissing(recherches, ...(this.editForm.get('recherches')!.value ?? []))
        )
      )
      .subscribe((recherches: IRecherche[]) => (this.recherchesSharedCollection = recherches));

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

  protected createFromForm(): IClient {
    return {
      ...new Client(),
      id: this.editForm.get(['id'])!.value,
      clientId: this.editForm.get(['clientId'])!.value,
      clientNom: this.editForm.get(['clientNom'])!.value,
      clientPrenom: this.editForm.get(['clientPrenom'])!.value,
      clientEmail: this.editForm.get(['clientEmail'])!.value,
      clientAdresse: this.editForm.get(['clientAdresse'])!.value,
      recherches: this.editForm.get(['recherches'])!.value,
      commande: this.editForm.get(['commande'])!.value,
    };
  }
}
