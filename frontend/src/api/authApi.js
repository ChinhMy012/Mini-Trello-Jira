import axiosClient from "./axiosClient";

export const authApi = {
  // 1. Đăng ký tài khoản [cite: 11]
  register: (data) => {
    // data bao gồm: { username, password, email } [cite: 13]
    return axiosClient.post("/api/users/register", data); // [cite: 13]
  },

  // 2. Đăng nhập [cite: 14]
  login: (data) => {
    // data bao gồm: { username, password } [cite: 15]
    return axiosClient.post("/api/users/login", data); // [cite: 15]
  },

  // 3. Đăng xuất [cite: 22]
  logout: () => {
    // Không cần gửi Request Body [cite: 23]
    return axiosClient.post("/api/users/logout"); // [cite: 23]
  },
};