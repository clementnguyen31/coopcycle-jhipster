import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRestaurateur, getRestaurateurIdentifier } from '../restaurateur.model';

export type EntityResponseType = HttpResponse<IRestaurateur>;
export type EntityArrayResponseType = HttpResponse<IRestaurateur[]>;

@Injectable({ providedIn: 'root' })
export class RestaurateurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/restaurateurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(restaurateur: IRestaurateur): Observable<EntityResponseType> {
    return this.http.post<IRestaurateur>(this.resourceUrl, restaurateur, { observe: 'response' });
  }

  update(restaurateur: IRestaurateur): Observable<EntityResponseType> {
    return this.http.put<IRestaurateur>(`${this.resourceUrl}/${getRestaurateurIdentifier(restaurateur) as number}`, restaurateur, {
      observe: 'response',
    });
  }

  partialUpdate(restaurateur: IRestaurateur): Observable<EntityResponseType> {
    return this.http.patch<IRestaurateur>(`${this.resourceUrl}/${getRestaurateurIdentifier(restaurateur) as number}`, restaurateur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRestaurateur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRestaurateur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRestaurateurToCollectionIfMissing(
    restaurateurCollection: IRestaurateur[],
    ...restaurateursToCheck: (IRestaurateur | null | undefined)[]
  ): IRestaurateur[] {
    const restaurateurs: IRestaurateur[] = restaurateursToCheck.filter(isPresent);
    if (restaurateurs.length > 0) {
      const restaurateurCollectionIdentifiers = restaurateurCollection.map(
        restaurateurItem => getRestaurateurIdentifier(restaurateurItem)!
      );
      const restaurateursToAdd = restaurateurs.filter(restaurateurItem => {
        const restaurateurIdentifier = getRestaurateurIdentifier(restaurateurItem);
        if (restaurateurIdentifier == null || restaurateurCollectionIdentifiers.includes(restaurateurIdentifier)) {
          return false;
        }
        restaurateurCollectionIdentifiers.push(restaurateurIdentifier);
        return true;
      });
      return [...restaurateursToAdd, ...restaurateurCollection];
    }
    return restaurateurCollection;
  }
}
