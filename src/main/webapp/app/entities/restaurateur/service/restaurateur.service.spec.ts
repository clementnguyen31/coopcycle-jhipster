import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRestaurateur, Restaurateur } from '../restaurateur.model';

import { RestaurateurService } from './restaurateur.service';

describe('Restaurateur Service', () => {
  let service: RestaurateurService;
  let httpMock: HttpTestingController;
  let elemDefault: IRestaurateur;
  let expectedResult: IRestaurateur | IRestaurateur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RestaurateurService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      restaurateurId: 0,
      restaurateurNom: 'AAAAAAA',
      restaurateurPrenom: 'AAAAAAA',
      restaurateurBoutique: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Restaurateur', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Restaurateur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Restaurateur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          restaurateurId: 1,
          restaurateurNom: 'BBBBBB',
          restaurateurPrenom: 'BBBBBB',
          restaurateurBoutique: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Restaurateur', () => {
      const patchObject = Object.assign({}, new Restaurateur());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Restaurateur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          restaurateurId: 1,
          restaurateurNom: 'BBBBBB',
          restaurateurPrenom: 'BBBBBB',
          restaurateurBoutique: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Restaurateur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRestaurateurToCollectionIfMissing', () => {
      it('should add a Restaurateur to an empty array', () => {
        const restaurateur: IRestaurateur = { id: 123 };
        expectedResult = service.addRestaurateurToCollectionIfMissing([], restaurateur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurateur);
      });

      it('should not add a Restaurateur to an array that contains it', () => {
        const restaurateur: IRestaurateur = { id: 123 };
        const restaurateurCollection: IRestaurateur[] = [
          {
            ...restaurateur,
          },
          { id: 456 },
        ];
        expectedResult = service.addRestaurateurToCollectionIfMissing(restaurateurCollection, restaurateur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Restaurateur to an array that doesn't contain it", () => {
        const restaurateur: IRestaurateur = { id: 123 };
        const restaurateurCollection: IRestaurateur[] = [{ id: 456 }];
        expectedResult = service.addRestaurateurToCollectionIfMissing(restaurateurCollection, restaurateur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurateur);
      });

      it('should add only unique Restaurateur to an array', () => {
        const restaurateurArray: IRestaurateur[] = [{ id: 123 }, { id: 456 }, { id: 44684 }];
        const restaurateurCollection: IRestaurateur[] = [{ id: 123 }];
        expectedResult = service.addRestaurateurToCollectionIfMissing(restaurateurCollection, ...restaurateurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const restaurateur: IRestaurateur = { id: 123 };
        const restaurateur2: IRestaurateur = { id: 456 };
        expectedResult = service.addRestaurateurToCollectionIfMissing([], restaurateur, restaurateur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurateur);
        expect(expectedResult).toContain(restaurateur2);
      });

      it('should accept null and undefined values', () => {
        const restaurateur: IRestaurateur = { id: 123 };
        expectedResult = service.addRestaurateurToCollectionIfMissing([], null, restaurateur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurateur);
      });

      it('should return initial array if no Restaurateur is added', () => {
        const restaurateurCollection: IRestaurateur[] = [{ id: 123 }];
        expectedResult = service.addRestaurateurToCollectionIfMissing(restaurateurCollection, undefined, null);
        expect(expectedResult).toEqual(restaurateurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
