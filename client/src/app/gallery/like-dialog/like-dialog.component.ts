import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';

@Component({
  selector: 'app-like-dialog',
  templateUrl: './like-dialog.component.html',
  styleUrls: ['./like-dialog.component.css']
})
export class LikeDialogComponent implements OnInit {
  @Input() image;

  constructor(public dialogRef: MatDialogRef<LikeDialogComponent>,
              @Inject(MAT_DIALOG_DATA) data: any) {
    this.image = data['image']
  }

  ngOnInit() {
    console.log(this.image)
  }

}
