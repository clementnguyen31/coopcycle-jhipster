import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RestaurateurService } from '../service/restaurateur.service';

import { RestaurateurComponent } from './restaurateur.component';

describe('Restaurateur Management Component', () => {
  let comp: RestaurateurComponent;
  let fixture: ComponentFixture<RestaurateurComponent>;
  let service: RestaurateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RestaurateurComponent],
    })
      .overrideTemplate(RestaurateurComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurateurComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RestaurateurService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.restaurateurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
