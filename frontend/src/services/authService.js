import api from "./api";

const authService = {
    //register new user
    signup: async (username, email, password) => {
        const response = await api.post('/auth/signup', { username, email, password });
        return response.data;
    },

    //login user
    login: async (username, password) => {
        const response = await api.post('/auth/login', { username, password });

        if(response.data.token){
            //save token and user info to local storage
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', JSON.stringify(response.data.user));
        }
        return response.data;
    },

    //logout user
    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    },

    //get current user info from local storage
    getCurrentUser: () => {
        const user = localStorage.getItem('user');
        if(user) {
            return JSON.parse(user);
        }
        return null;
    },

    //check if user is logged in
    isAuthenticated: () => {
        return !!localStorage.getItem('token');
    },

};
export default authService;