import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from "./login/login.component";
import {HttpClientModule} from "@angular/common/http";
import {GalleryComponent} from "./gallery/gallery.component";
import {ToolbarComponent} from './toolbar/toolbar.component';
import {BrowserComponent} from './browser/browser.component';
import {BrowserControlsComponent} from './browser-controls/browser-controls.component';
import {FormsModule} from "@angular/forms";
import { GroupsComponent } from './groups/groups.component';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule
  ],
  declarations: [ LoginComponent, GalleryComponent, ToolbarComponent, BrowserComponent, BrowserControlsComponent, GroupsComponent ],
  exports: [ LoginComponent, GalleryComponent, GroupsComponent ]
})
export class GalleryModule { }
