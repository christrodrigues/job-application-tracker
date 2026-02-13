import { useEffect, useState } from "react";
import jobApplicationService from "../services/jobApplicationService";
import toast from "react-hot-toast";

const StatisticsCards = () => {
  const [stats, setStats] = useState({
    total: 0,
    applied: 0,
    interview: 0,
    offer: 0,
    rejected: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchStatistics();
  }, []);

  const fetchStatistics = async () => {
    try {
      const data = await jobApplicationService.getStatistics();
      setStats(data);
    } catch (error) {
      console.error("Error fetching statistics:", error);
      toast.error("Failed to load statistics");
    } finally {
      setLoading(false);
    }
  };

  const cards = [
    { label: "Total Applications", value: stats.total, color: "bg-blue-500" },
    { label: "Applied", value: stats.applied, color: "bg-yellow-500" },
    { label: "Interview", value: stats.interview, color: "bg-purple-500" },
    { label: "Offers", value: stats.offer, color: "bg-green-500" },
    { label: "Rejected", value: stats.rejected, color: "bg-red-500" },
  ];

  if (loading) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-5 gap-4 mb-6">
        {[...Array(5)].map((_, i) => (
          <div key={i} className="bg-white rounded-lg shadow p-6 animate-pulse">
            <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
            <div className="h-8 bg-gray-200 rounded w-1/2"></div>
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-5 gap-4 mb-6">
      {cards.map((card, index) => (
        <div
          key={index}
          className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">{card.label}</p>
              <p className="text-3xl font-bold text-gray-900 mt-2">
                {card.value}
              </p>
            </div>
            <div
              className={`${card.color} w-12 h-12 rounded-full flex items-center justify-center`}
            >
              <span className="text-white text-xl font-bold">{card.value}</span>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};
export default StatisticsCards;
