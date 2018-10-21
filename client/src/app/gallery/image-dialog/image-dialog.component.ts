import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {LikeDialogComponent} from '../like-dialog/like-dialog.component';
import {Image} from '../entity/Image';

@Component({
  selector: 'app-image-dialog',
  templateUrl: './image-dialog.component.html',
  styleUrls: ['./image-dialog.component.css']
})
export class ImageDialogComponent implements OnInit {
  @Input() imageUrl;
  @Input() image: Image;

  constructor(public dialogRef: MatDialogRef<ImageDialogComponent>,
              @Inject(MAT_DIALOG_DATA) data: any, private dialog: MatDialog) {
    this.imageUrl = data['imageUrl'];
    this.image = data['image']
  }

  ngOnInit() {
    console.log(this.imageUrl)
  }

  openLikeDialog() {
    this.dialog.open(LikeDialogComponent, {
      data: {
        image: this.image
      }
    });
  }
}
