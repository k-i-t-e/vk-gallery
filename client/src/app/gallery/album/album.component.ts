import {Component, OnInit} from '@angular/core';
import {AlbumService} from '../service/album/album.service';
import {ActivatedRoute} from '@angular/router';
import {flatMap} from 'rxjs/operators';
import {Image} from '../entity/Image';
import {ImageDialogComponent} from '../image-dialog/image-dialog.component';
import {MatDialog} from '@angular/material';
import {AppUtils} from '../utils/app-utils.service';

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  styleUrls: ['./album.component.css']
})
export class AlbumComponent implements OnInit {
  thumbnails: Array<string> = [];
  images: Array<Image> = [];
  constructor(private albumService: AlbumService,
              private route: ActivatedRoute,
              private appUtils: AppUtils,
              private dialog: MatDialog) {
  }

  ngOnInit() {
    this.route.params
      .pipe(
        flatMap(params => this.albumService.getAlbumImages(params.id))
      )
      .subscribe(images => {
        this.thumbnails = images.map(i => i.thumbnail);
        this.images = images;
      })
  }

  openModal(index: number) {
    if (index < this.images.length) {
      this.dialog.open(ImageDialogComponent, {
        data: {
          imageUrl: this.appUtils.getLargeImage(this.images[index]),
          image: this.images[index]
        }
      })
    }
  }
}
