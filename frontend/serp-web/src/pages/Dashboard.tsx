import { useEffect, useState } from 'react'
import api from '../api/client'

export default function Dashboard(){
  const [kpis, setKpis] = useState({ customers:0, consultants:0, projects:0, entries:0 })
  useEffect(()=>{
    (async ()=>{
      const [cs, cons, ps, es] = await Promise.all([
        api.get('/api/customers'), api.get('/api/consultants'),
        api.get('/api/projects'), api.get('/api/entries')
      ])
      setKpis({ customers: cs.data.length, consultants: cons.data.length, projects: ps.data.length, entries: es.data.length })
    })()
  },[])
  return (
    <div style={{padding:16, display:'grid', gridTemplateColumns:'repeat(4, 1fr)', gap:12}}>
      <div style={{border:'1px solid #ddd', padding:12, borderRadius:8}}><h3>Kunden</h3><strong>{kpis.customers}</strong></div>
      <div style={{border:'1px solid #ddd', padding:12, borderRadius:8}}><h3>Consultants</h3><strong>{kpis.consultants}</strong></div>
      <div style={{border:'1px solid #ddd', padding:12, borderRadius:8}}><h3>Projekte</h3><strong>{kpis.projects}</strong></div>
      <div style={{border:'1px solid #ddd', padding:12, borderRadius:8}}><h3>Einträge</h3><strong>{kpis.entries}</strong></div>
    </div>
  )
}
