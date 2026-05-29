/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
export type Account = {
  id: string;
  name: string;
  email: string;
  username: string;
};

export type ApiResponse<TData> = {
  success: boolean;
  message: string;
  data: TData;
};

export type SigninRequest = {
  username: string;
  password: string;
};

export type SignupRequest = {
  name: string;
  email: string;
  username: string;
  password: string;
  invitationCode?: string;
};

export type TokenResponse = {
  token: string;
  userInfo: Account;
};

export type SignupResponse = Account;
