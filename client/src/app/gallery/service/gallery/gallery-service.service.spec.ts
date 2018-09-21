import { TestBed } from '@angular/core/testing';

import { GalleryService } from './gallery-service.service';

describe('GalleryService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GalleryService = TestBed.get(GalleryService);
    expect(service).toBeTruthy();
  });
});
