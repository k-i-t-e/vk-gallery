import { Component, OnInit } from '@angular/core';
import {AlbumService} from '../service/album/album.service';
import {Album} from '../entity/Album';
import {AppUtils} from '../utils/app-utils.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-albums',
  templateUrl: './albums.component.html',
  styleUrls: ['./albums.component.css']
})
export class AlbumsComponent implements OnInit {
  albums: Array<Album> = [];
  constructor(private albumService: AlbumService,
              public appUtils: AppUtils,
              private router: Router) { }

  ngOnInit() {
    this.albumService.getAlbums().subscribe(res => this.albums = res);
  }

  openAlbum(album: Album) {
    this.router.navigateByUrl(`/albums/${album.id}`)
  }

}
