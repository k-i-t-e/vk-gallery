import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {LoginComponent} from "./login/login.component";
import {HttpClientModule} from "@angular/common/http";
import {GalleryComponent} from "./gallery/gallery.component";

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule
  ],
  declarations: [ LoginComponent, GalleryComponent ],
  exports: [ LoginComponent, GalleryComponent ]
})
export class GalleryModule { }
