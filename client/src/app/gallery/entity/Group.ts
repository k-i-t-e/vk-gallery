export class GroupResult {
  favourites: Array<Group>;
  allGroups: Array<Group>
}

export class Group {
  id: number;
  domain: string;
  name: string;
  alias: string;
  imageUrl?: string
}
