export class User {
  public userId: string;
  public firstName: string;
  public lastName: string;
  public username: string;
  public email: string;
  public profileImageUrl: string;
  public lastLoginDate: Date;
  public lastLoginDateDisplay: Date;
  public joinDate: Date;
  public role;
  public authorities: [];
  public active: boolean;
  public notLocked: boolean;

  constructor() {
    this.firstName = "";
    this.lastName = "";
    this.username = "";
    this.email = "";
    this.active = false;
    this.notLocked = false;
    this.role = "";
    this.authorities = [];
  }
}
