import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewAlbumDialogComponent } from './new-album-dialog.component';

describe('NewAlbumDialogComponent', () => {
  let component: NewAlbumDialogComponent;
  let fixture: ComponentFixture<NewAlbumDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewAlbumDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewAlbumDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
