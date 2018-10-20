import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Album} from '../entity/Album';
import {AlbumService} from '../service/album/album.service';

@Component({
  selector: 'app-like-dialog',
  templateUrl: './like-dialog.component.html',
  styleUrls: ['./like-dialog.component.css']
})
export class LikeDialogComponent implements OnInit {
  @Input() image;
  albums: Array<Album> = [new Album(1, 'test'), new Album(2, 'ololo')];
  selectedAlbum: Album;
  albumName: string;
  showOptions = false;

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
      console.log('Album already exists')
    } else {
      console.log(this.albumName)
    }
  }

  selectOption(album: Album) {
    this.selectedAlbum = album;
    this.albumName = album.name;
    this.doShowOptions();
  }
}
