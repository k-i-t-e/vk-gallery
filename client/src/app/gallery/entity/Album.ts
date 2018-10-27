import {Image} from "./Image";

export class Album {
  id: number;
  name: string;
  cover: Image;

  constructor(id: number, name: string) {
    this.id = id;
    this.name = name;
  }
}
