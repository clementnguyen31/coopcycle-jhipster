import { ILivreur } from 'app/entities/livreur/livreur.model';
import { IRestaurateur } from 'app/entities/restaurateur/restaurateur.model';
import { IClient } from 'app/entities/client/client.model';

export interface ICooperative {
  id?: number;
  cooperativeId?: number | null;
  cooperativeNom?: string | null;
  cooperativeDirecteur?: string | null;
  cooperativeVille?: string | null;
  cooperativeMetropole?: string | null;
  cooperativeCommune?: string | null;
  livreurs?: ILivreur[] | null;
  restaurateurs?: IRestaurateur[] | null;
  clients?: IClient[] | null;
}

export class Cooperative implements ICooperative {
  constructor(
    public id?: number,
    public cooperativeId?: number | null,
    public cooperativeNom?: string | null,
    public cooperativeDirecteur?: string | null,
    public cooperativeVille?: string | null,
    public cooperativeMetropole?: string | null,
    public cooperativeCommune?: string | null,
    public livreurs?: ILivreur[] | null,
    public restaurateurs?: IRestaurateur[] | null,
    public clients?: IClient[] | null
  ) {}
}

export function getCooperativeIdentifier(cooperative: ICooperative): number | undefined {
  return cooperative.id;
}
