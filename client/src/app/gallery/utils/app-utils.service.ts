import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AppUtils {
  getRange(n: number) {
    let arr = Array.from(Array(Math.ceil(n)).keys());
    return arr
  }
}
