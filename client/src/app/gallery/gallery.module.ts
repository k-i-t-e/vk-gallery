import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from './login/login.component';
import {HttpClientModule} from '@angular/common/http';
import {GalleryComponent} from './gallery/gallery.component';
import {ToolbarComponent} from './toolbar/toolbar.component';
import {BrowserComponent} from './browser/browser.component';
import {BrowserControlsComponent} from './browser-controls/browser-controls.component';
import {FormsModule} from '@angular/forms';
import {GroupsComponent} from './groups/groups.component';
import {AppRoutingModule} from '../app-routing.module';
import {ImageDialogComponent} from './image-dialog/image-dialog.component';
import {MatButtonModule, MatDialogModule} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { LikeDialogComponent } from './like-dialog/like-dialog.component';
import {EllipsisPipe} from './pipe/ellipsis.pipe';
import { AlbumsComponent } from './albums/albums.component';
import { ThumbnailsComponent } from './thumbnails/thumbnails.component';
import { AlbumComponent } from './album/album.component';
import { NewAlbumDialogComponent } from './new-album-dialog/new-album-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    MatDialogModule,
    MatButtonModule,
    BrowserAnimationsModule
  ],
  declarations: [LoginComponent, GalleryComponent, ToolbarComponent, BrowserComponent, BrowserControlsComponent,
                 GroupsComponent, ImageDialogComponent, LikeDialogComponent, EllipsisPipe, AlbumsComponent,
                 ThumbnailsComponent, AlbumComponent, NewAlbumDialogComponent ],
  exports: [ LoginComponent, GalleryComponent, GroupsComponent ],
  entryComponents: [ImageDialogComponent, LikeDialogComponent, NewAlbumDialogComponent ]
})
export class GalleryModule { }
