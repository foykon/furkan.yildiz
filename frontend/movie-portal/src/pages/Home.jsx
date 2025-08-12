import { useEffect, useState } from "react";
import { api, endpoints } from "../lib/api";
import HeroSlider from "../components/HeroSlider.jsx";
import RowSection from "../components/RowSection.jsx";

export default function Home(){
  const [featured, setFeatured] = useState([]);

  useEffect(() => {
    const pageable = { page: 0, size: 5, sort: ["releaseDate,desc"] };
    api.get(endpoints.movies.search, { params: { filter: {}, pageable } })
      .then(res => setFeatured(res.data?.data || []));
  }, []);

  return (
    <div>
      <HeroSlider items={featured} />

      <RowSection
        title="Latest Movies"
        sort="releaseDate,desc"
        size={7}
        cardWidth={240}
      />

      <RowSection
        title="Top Rated"
        sort="rating,desc"
        size={7}
        cardWidth={240}
      />

      <RowSection
        title="Suggested For You"
        sort="title,desc"
        size={7}
        cardWidth={240}
        transform={(arr)=> arr.slice().sort(()=> Math.random()-0.5)}
      />
    </div>
  );
}
