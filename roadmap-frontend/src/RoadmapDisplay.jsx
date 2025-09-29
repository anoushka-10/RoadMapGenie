import { useEffect, useState } from "react";
import mermaid from "mermaid";

export default function RoadmapDisplay() {
  const [data, setData] = useState(null);
  const [mermaidSvg, setMermaidSvg] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [topic, setTopic] = useState("Spring Boot");
  const [inputTopic, setInputTopic] = useState("Spring Boot");

  useEffect(() => {
    mermaid.initialize({ 
      startOnLoad: false,
      theme: 'default',
      securityLevel: 'loose',
    });
  }, []);

  const fetchRoadmap = async (searchTopic) => {
    setLoading(true);
    setError(null);
    
    try {
      const roadmapRes = await fetch(
        `http://localhost:8080/ai/roadmap?topic=${encodeURIComponent(searchTopic)}`
      );
      
      if (!roadmapRes.ok) {
        throw new Error(`HTTP error! status: ${roadmapRes.status}`);
      }
      
      const roadmapData = await roadmapRes.json();
      setData(roadmapData);

      const mermaidRes = await fetch(
        `http://localhost:8080/ai/mermaid?topic=${encodeURIComponent(searchTopic)}`
      );
      
      if (!mermaidRes.ok) {
        throw new Error(`HTTP error! status: ${mermaidRes.status}`);
      }
      
      const mermaidCode = await mermaidRes.text();
      const { svg } = await mermaid.render('mermaid-diagram', mermaidCode);
      setMermaidSvg(svg);
      
    } catch (err) {
      console.error("Error fetching roadmap:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRoadmap(topic);
  }, []);

  const handleSearch = () => {
    if (inputTopic.trim()) {
      setTopic(inputTopic);
      fetchRoadmap(inputTopic);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center">
        <div className="text-center">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
          <p className="mt-4 text-lg text-gray-700">Generating your roadmap...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
        <div className="bg-red-50 border border-red-200 rounded-lg p-6 max-w-md">
          <h3 className="text-red-800 font-semibold text-lg mb-2">Error Loading Roadmap</h3>
          <p className="text-red-600">{error}</p>
          <button 
            onClick={() => fetchRoadmap(topic)}
            className="mt-4 bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700"
          >
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 py-6">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            🗺️ Roadmap Genie
          </h1>
          
          <div className="flex gap-2">
            <input
              type="text"
              value={inputTopic}
              onChange={(e) => setInputTopic(e.target.value)}
              onKeyPress={handleKeyPress}
              placeholder="Enter a topic (e.g., Docker, React, Python...)"
              className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
            <button
              onClick={handleSearch}
              className="bg-indigo-600 text-white px-6 py-2 rounded-lg hover:bg-indigo-700 transition-colors font-medium"
            >
              Generate
            </button>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 py-8">
        {data && (
          <>
            <div className="bg-white rounded-lg shadow-md p-6 mb-6">
              <h2 className="text-3xl font-bold text-indigo-900">
                {data.topic} Learning Roadmap
              </h2>
            </div>

            {mermaidSvg && (
              <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                <h3 className="text-xl font-semibold text-gray-800 mb-4">
                  📊 Visual Learning Path
                </h3>
                <div 
                  className="overflow-x-auto flex justify-center"
                  dangerouslySetInnerHTML={{ __html: mermaidSvg }}
                />
              </div>
            )}

            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-xl font-semibold text-gray-800 mb-4">
                📚 Detailed Learning Steps
              </h3>
              
              <div className="space-y-6">
                {data.roadmap.map((step, idx) => (
                  <div 
                    key={idx} 
                    className="border-l-4 border-indigo-500 pl-4 py-2"
                  >
                    <h4 className="text-lg font-semibold text-gray-900 mb-2">
                      {step.step}
                    </h4>
                    <p className="text-gray-700 mb-3">{step.description}</p>
                    
                    {step.resources && step.resources.length > 0 && (
                      <div>
                        <p className="text-sm font-medium text-gray-600 mb-2">
                          Resources:
                        </p>
                        <ul className="list-disc list-inside space-y-1">
                          {step.resources.map((link, j) => (
                            <li key={j} className="text-indigo-600">
                              <a 
                                href={link} 
                                target="_blank" 
                                rel="noreferrer"
                                className="hover:underline break-all"
                              >
                                {link}
                              </a>
                            </li>
                          ))}
                        </ul>
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  );
}