import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {LoginComponent} from "./login/login.component";
import {HttpClientModule} from "@angular/common/http";
import {GalleryComponent} from "./gallery/gallery.component";
import { ToolbarComponent } from './toolbar/toolbar.component';
import { BrowserComponent } from './browser/browser.component';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule
  ],
  declarations: [ LoginComponent, GalleryComponent, ToolbarComponent, BrowserComponent ],
  exports: [ LoginComponent, GalleryComponent ]
})
export class GalleryModule { }
