import { IRecherche } from 'app/entities/recherche/recherche.model';
import { ICommande } from 'app/entities/commande/commande.model';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';
import { IRestaurateur } from 'app/entities/restaurateur/restaurateur.model';

export interface IClient {
  id?: number;
  clientId?: number | null;
  clientNom?: string | null;
  clientPrenom?: string | null;
  clientEmail?: string | null;
  clientAdresse?: string | null;
  recherches?: IRecherche[] | null;
  commande?: ICommande | null;
  cooperatives?: ICooperative[] | null;
  restaurateurs?: IRestaurateur[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public clientId?: number | null,
    public clientNom?: string | null,
    public clientPrenom?: string | null,
    public clientEmail?: string | null,
    public clientAdresse?: string | null,
    public recherches?: IRecherche[] | null,
    public commande?: ICommande | null,
    public cooperatives?: ICooperative[] | null,
    public restaurateurs?: IRestaurateur[] | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
