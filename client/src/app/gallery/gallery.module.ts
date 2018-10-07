import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from './login/login.component';
import {HttpClientModule} from '@angular/common/http';
import {GalleryComponent} from './gallery/gallery.component';
import {ToolbarComponent} from './toolbar/toolbar.component';
import {BrowserComponent} from './browser/browser.component';
import {BrowserControlsComponent} from './browser-controls/browser-controls.component';
import {FormsModule} from '@angular/forms';
import { GroupsComponent } from './groups/groups.component';
import {AppRoutingModule} from '../app-routing.module';
import { ImageModalComponent } from './image-modal/image-modal.component';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {LightboxModule} from 'ngx-lightbox';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    NgbModalModule,
    LightboxModule
  ],
  declarations: [ LoginComponent, GalleryComponent, ToolbarComponent, BrowserComponent, BrowserControlsComponent,
                  GroupsComponent, ImageModalComponent ],
  exports: [ LoginComponent, GalleryComponent, GroupsComponent ],
  entryComponents: [ ImageModalComponent ]
})
export class GalleryModule { }
