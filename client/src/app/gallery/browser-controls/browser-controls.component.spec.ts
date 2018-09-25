import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowserControlsComponent } from './browser-controls.component';

describe('BrowserControlsComponent', () => {
  let component: BrowserControlsComponent;
  let fixture: ComponentFixture<BrowserControlsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BrowserControlsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BrowserControlsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
