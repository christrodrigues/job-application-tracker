import api from './api';

const jobApplicationService = {
  // Get all applications with pagination and filters
  getAll: async (params = {}) => {
    const response = await api.get('/applications', { params });
    return response.data;
  },

  // Get single application by ID
  getById: async (id) => {
    const response = await api.get(`/applications/${id}`);
    return response.data;
  },

  // Create new application
  create: async (applicationData) => {
    const response = await api.post('/applications', applicationData);
    return response.data;
  },

  // Update application
  update: async (id, applicationData) => {
    const response = await api.put(`/applications/${id}`, applicationData);
    return response.data;
  },

  // Delete application
  delete: async (id) => {
    const response = await api.delete(`/applications/${id}`);
    return response.data;
  },

  // Get statistics
  getStatistics: async () => {
    const response = await api.get('/applications/stats');
    return response.data;
  },
};

export default jobApplicationService;