import { useEffect, useMemo, useRef, useState } from "react";
import { Select, Spin, message } from "antd";
import { api, endpoints } from "../lib/api";

export default function ActorSelect({ value, onChange, allowClear = true, placeholder = "Select actor", style }) {
  const [options, setOptions] = useState([]);
  const [fetching, setFetching] = useState(false);
  const debRef = useRef();

  const load = async (q = "") => {
    setFetching(true);
    try {
      const params = {
        q,
        "pageable.page": 0,
        "pageable.size": 10,
      };
      const { data } = await api.get(endpoints.actors.list, { params });
      const arr = data?.data ?? [];
      setOptions(arr.map(a => ({ label: a.name, value: a.id })));
    } catch (e) {
      message.error("Actors fetch failed");
    } finally {
      setFetching(false);
    }
  };

  useEffect(() => { load(""); }, []);

  useEffect(() => {
    (async () => {
      if (!value) return;
      const exists = options.some(o => o.value === value);
      if (exists) return;
      try {
        const { data } = await api.get(endpoints.actors.get ? endpoints.actors.get(value) : `/api/v1/actors/${value}`);
        const a = data?.data;
        if (a) setOptions(prev => [{ label: a.name, value: a.id }, ...prev]);
      } catch {}
    })();
  }, [value]);

  const handleSearch = (q) => {
    clearTimeout(debRef.current);
    debRef.current = setTimeout(() => load(q), 300);
  };

  return (
    <Select
      showSearch
      allowClear={allowClear}
      value={value ?? undefined}
      onChange={onChange}
      onSearch={handleSearch}
      filterOption={false}
      options={options}
      placeholder={placeholder}
      notFoundContent={fetching ? <Spin size="small" /> : null}
      style={style}
    />
  );
}
