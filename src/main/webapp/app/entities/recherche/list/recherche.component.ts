import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecherche } from '../recherche.model';
import { RechercheService } from '../service/recherche.service';
import { RechercheDeleteDialogComponent } from '../delete/recherche-delete-dialog.component';

@Component({
  selector: 'jhi-recherche',
  templateUrl: './recherche.component.html',
})
export class RechercheComponent implements OnInit {
  recherches?: IRecherche[];
  isLoading = false;

  constructor(protected rechercheService: RechercheService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.rechercheService.query().subscribe({
      next: (res: HttpResponse<IRecherche[]>) => {
        this.isLoading = false;
        this.recherches = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRecherche): number {
    return item.id!;
  }

  delete(recherche: IRecherche): void {
    const modalRef = this.modalService.open(RechercheDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.recherche = recherche;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
