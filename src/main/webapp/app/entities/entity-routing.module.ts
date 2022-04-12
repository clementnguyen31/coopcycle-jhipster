import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cooperative',
        data: { pageTitle: 'myapplicationApp.cooperative.home.title' },
        loadChildren: () => import('./cooperative/cooperative.module').then(m => m.CooperativeModule),
      },
      {
        path: 'restaurateur',
        data: { pageTitle: 'myapplicationApp.restaurateur.home.title' },
        loadChildren: () => import('./restaurateur/restaurateur.module').then(m => m.RestaurateurModule),
      },
      {
        path: 'livreur',
        data: { pageTitle: 'myapplicationApp.livreur.home.title' },
        loadChildren: () => import('./livreur/livreur.module').then(m => m.LivreurModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'myapplicationApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'commande',
        data: { pageTitle: 'myapplicationApp.commande.home.title' },
        loadChildren: () => import('./commande/commande.module').then(m => m.CommandeModule),
      },
      {
        path: 'recherche',
        data: { pageTitle: 'myapplicationApp.recherche.home.title' },
        loadChildren: () => import('./recherche/recherche.module').then(m => m.RechercheModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
