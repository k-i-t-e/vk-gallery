import {Component, OnInit} from '@angular/core';
import {AlbumService} from '../service/album/album.service';
import {ActivatedRoute} from '@angular/router';
import {flatMap} from 'rxjs/operators';

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  styleUrls: ['./album.component.css']
})
export class AlbumComponent implements OnInit {
  images: Array<string> = [];
  constructor(private albumService: AlbumService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params
      .pipe(
        flatMap(params => this.albumService.getAlbumImages(params.id))
      )
      .subscribe(images => this.images = images.map(i => i.thumbnail))
  }
}
