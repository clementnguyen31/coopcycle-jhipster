import { IClient } from 'app/entities/client/client.model';
import { ICommande } from 'app/entities/commande/commande.model';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';

export interface IRestaurateur {
  id?: number;
  restaurateurId?: number | null;
  restaurateurNom?: string | null;
  restaurateurPrenom?: string | null;
  restaurateurBoutique?: string | null;
  clients?: IClient[] | null;
  commande?: ICommande | null;
  cooperatives?: ICooperative[] | null;
}

export class Restaurateur implements IRestaurateur {
  constructor(
    public id?: number,
    public restaurateurId?: number | null,
    public restaurateurNom?: string | null,
    public restaurateurPrenom?: string | null,
    public restaurateurBoutique?: string | null,
    public clients?: IClient[] | null,
    public commande?: ICommande | null,
    public cooperatives?: ICooperative[] | null
  ) {}
}

export function getRestaurateurIdentifier(restaurateur: IRestaurateur): number | undefined {
  return restaurateur.id;
}
