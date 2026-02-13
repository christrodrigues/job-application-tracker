import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import jobApplicationService from "../services/jobApplicationService";
import toast from "react-hot-toast";
import StatisticsCards from "../components/StatisticsCards";
import ApplicationsTable from "../components/ApplicationsTable";
import ApplicationModal from "../components/ApplicationModal";

const Dashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingApplication, setEditingApplication] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [refreshStats, setRefreshStats] = useState(0);

  useEffect(() => {
    fetchApplications();
  }, [currentPage, searchTerm, statusFilter]);

  const fetchApplications = async () => {
    setLoading(true);
    try {
      const params = {
        page: currentPage,
        size: 10,
        sortBy: "dateApplied",
        direction: "desc",
      };

      if (searchTerm) {
        params.keyword = searchTerm;
      }

      if (statusFilter) {
        params.status = statusFilter;
      }

      const response = await jobApplicationService.getAll(params);
      setApplications(response.content);
      setTotalPages(response.totalPages);
    } catch (error) {
      console.error("Error fetching applications:", error);
      toast.error("Failed to load applications");
    } finally {
      setLoading(false);
    }
  };

  const handleCreateOrUpdate = async (formData) => {
    try {
      if (editingApplication) {
        // Update existing
        await jobApplicationService.update(editingApplication.id, formData);
        toast.success("Application updated successfully!");
      } else {
        // Create new
        await jobApplicationService.create(formData);
        toast.success("Application created successfully!");
      }

      setIsModalOpen(false);
      setEditingApplication(null);
      fetchApplications();
      setRefreshStats((prev) => prev + 1); // Trigger stats refresh
    } catch (error) {
      console.error("Error saving application:", error);
      const errorMessage =
        error.response?.data?.message || "Failed to save application";
      toast.error(errorMessage);
    }
  };

  const handleEdit = (application) => {
    setEditingApplication(application);
    setIsModalOpen(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this application?")) {
      return;
    }

    try {
      await jobApplicationService.delete(id);
      toast.success("Application deleted successfully!");
      fetchApplications();
      setRefreshStats((prev) => prev + 1); // Trigger stats refresh
    } catch (error) {
      console.error("Error deleting application:", error);
      toast.error("Failed to delete application");
    }
  };

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const handleNewApplication = () => {
    setEditingApplication(null);
    setIsModalOpen(true);
  };

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
    setCurrentPage(0); // Reset to first page
  };

  const handleStatusFilter = (e) => {
    setStatusFilter(e.target.value);
    setCurrentPage(0); // Reset to first page
  };

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Navigation */}
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <h1 className="text-xl font-bold text-gray-900">
                Job Application Tracker
              </h1>
            </div>
            <div className="flex items-center space-x-4">
              <span className="text-gray-700">
                Welcome, <span className="font-semibold">{user?.username}</span>
                !
              </span>
              <button
                onClick={handleLogout}
                className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          {/* Statistics Cards */}
          <StatisticsCards key={refreshStats} />

          {/* Filters and Actions */}
          <div className="mb-6 flex flex-col md:flex-row md:items-center md:justify-between space-y-4 md:space-y-0">
            <div className="flex flex-col md:flex-row space-y-4 md:space-y-0 md:space-x-4 flex-1">
              {/* Search */}
              <input
                type="text"
                placeholder="Search by company or role..."
                value={searchTerm}
                onChange={handleSearch}
                className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 w-full md:w-64"
              />

              {/* Status Filter */}
              <select
                value={statusFilter}
                onChange={handleStatusFilter}
                className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 w-full md:w-48"
              >
                <option value="">All Statuses</option>
                <option value="APPLIED">Applied</option>
                <option value="INTERVIEW">Interview</option>
                <option value="OFFER">Offer</option>
                <option value="REJECTED">Rejected</option>
              </select>
            </div>

            {/* New Application Button */}
            <button
              onClick={handleNewApplication}
              className="bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-2 rounded-md font-medium transition-colors shadow-sm"
            >
              + New Application
            </button>
          </div>

          {/* Applications Table */}
          <ApplicationsTable
            applications={applications}
            onEdit={handleEdit}
            onDelete={handleDelete}
            loading={loading}
          />

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="mt-6 flex justify-center items-center space-x-2">
              <button
                onClick={() => setCurrentPage((prev) => Math.max(0, prev - 1))}
                disabled={currentPage === 0}
                className="px-4 py-2 border border-gray-300 rounded-md disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
              >
                Previous
              </button>

              <span className="text-gray-700">
                Page {currentPage + 1} of {totalPages}
              </span>

              <button
                onClick={() =>
                  setCurrentPage((prev) => Math.min(totalPages - 1, prev + 1))
                }
                disabled={currentPage === totalPages - 1}
                className="px-4 py-2 border border-gray-300 rounded-md disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
              >
                Next
              </button>
            </div>
          )}
        </div>
      </main>

      {/* Application Modal */}
      <ApplicationModal
        isOpen={isModalOpen}
        onClose={() => {
          setIsModalOpen(false);
          setEditingApplication(null);
        }}
        onSave={handleCreateOrUpdate}
        application={editingApplication}
      />
    </div>
  );
};

export default Dashboard;
