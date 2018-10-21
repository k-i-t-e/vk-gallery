import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Album} from '../entity/Album';
import {AlbumService} from '../service/album/album.service';
import {Image} from '../entity/Image';

@Component({
  selector: 'app-like-dialog',
  templateUrl: './like-dialog.component.html',
  styleUrls: ['./like-dialog.component.css']
})
export class LikeDialogComponent implements OnInit {
  @Input() image: Image;
  albums: Array<Album>;
  selectedAlbum: Album;
  albumName: string;
  showOptions = false;
  errorMessage: string;

  constructor(public dialogRef: MatDialogRef<LikeDialogComponent>,
              @Inject(MAT_DIALOG_DATA) data: any, private albumService: AlbumService) {
    this.image = data['image']
  }

  ngOnInit() {
    this.albumService.getAlbums().subscribe(albums => this.albums = albums)
  }

  doShowOptions() {
    this.showOptions = !this.showOptions
  }

  addAlbum() {
    if (this.albums.find(a => a.name === this.albumName)) {
      this.errorMessage = 'Album already exists'
    } else {
      this.errorMessage = undefined;
      this.albumService.addAlbum(this.albumName).subscribe(
        albums => this.albums = albums,
        this.handleError
      );
    }
  }

  onCancel() {
    this.dialogRef.close()
  }

  onOk() {
    console.log(this.image);
    console.log(this.selectedAlbum);

    let albumId: number;
    if (this.albumName) {
      if (this.selectedAlbum) {
        albumId = this.selectedAlbum.id
      } else {
        const albumByName = this.albums.find(a => a.name === this.albumName);
        if (albumByName) {
          albumId = albumByName.id
        } else {
          this.errorMessage = `No album with name ${this.albumName} exists`;
          return
        }
      }

      this.albumService.addImage(albumId, this.image).subscribe(
        () => {
          this.dialogRef.close();
        },
        this.handleError);
    } else {
      this.errorMessage = 'Please select the album'
    }
  }

  selectOption(album: Album) {
    this.selectedAlbum = album;
    this.albumName = album.name;
    this.doShowOptions();
    this.errorMessage = undefined;

    console.log(this.selectedAlbum);
  }

  onChangeName() {
    this.selectedAlbum = undefined;
    console.log(this.selectedAlbum);
  }

  private handleError = (e) => {
    console.log(e);
    this.errorMessage = e.error.message
  }
}
