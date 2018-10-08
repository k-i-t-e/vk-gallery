import {Component, OnInit} from '@angular/core';
import {GalleryService} from '../service/gallery/gallery.service';
import {Image} from '../entity/Image';
import {AppUtils} from '../utils/app-utils.service';
import {MatDialog} from '@angular/material';
import {ImageDialogComponent} from '../image-dialog/image-dialog.component';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-browser',
  templateUrl: './browser.component.html',
  styleUrls: ['./browser.component.css']
})
export class BrowserComponent implements OnInit {
  images: Array<Image> = [];

  totalPages = 1;
  page = 0;
  groupId: string;

  constructor(private galleryService: GalleryService, public appUtils: AppUtils,
              private dialog: MatDialog, private route: ActivatedRoute) { }

  ngOnInit() {
  }

  getThumbnail(index: number): string {
    if (index < this.images.length) {
      return this.images[index].urls['604'];
    }
    return null;
  }

  getLargeImage(index: number): string {
    let largest: string;
    const entries = new Map(Object.entries(this.images[index].urls));

    for (const url of entries.keys()) {
      if (entries.get(url)) {
        largest = url
      } else {
        return entries.get(largest)
      }
      console.log(largest)
    }

    return entries.get(largest)
  }

  onPageSelected(n: number) {
    if (this.groupId) {
      this.page = n;
      this.galleryService.getImages(this.groupId, n * 100)
        .subscribe(res => {
          this.images = res.images;
          console.log(n);
          if (n + 1 > this.totalPages) {
            this.totalPages = n + 1
          }
        })
    }
  }

  onBrowse(groupId: string) {
    if (groupId) {
      this.page = 0;
      this.totalPages = 1;
      this.groupId = groupId;
      this.onPageSelected(0);
    }
  }

  openModal(index: number) {
    if (index < this.images.length) {
      this.dialog.open(ImageDialogComponent, {
        data: {
          imageUrl: this.getLargeImage(index)
        }
      })
    }
  }
}
