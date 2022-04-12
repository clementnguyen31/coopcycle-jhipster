import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRestaurateur } from '../restaurateur.model';
import { RestaurateurService } from '../service/restaurateur.service';
import { RestaurateurDeleteDialogComponent } from '../delete/restaurateur-delete-dialog.component';

@Component({
  selector: 'jhi-restaurateur',
  templateUrl: './restaurateur.component.html',
})
export class RestaurateurComponent implements OnInit {
  restaurateurs?: IRestaurateur[];
  isLoading = false;

  constructor(protected restaurateurService: RestaurateurService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.restaurateurService.query().subscribe({
      next: (res: HttpResponse<IRestaurateur[]>) => {
        this.isLoading = false;
        this.restaurateurs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRestaurateur): number {
    return item.id!;
  }

  delete(restaurateur: IRestaurateur): void {
    const modalRef = this.modalService.open(RestaurateurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.restaurateur = restaurateur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
