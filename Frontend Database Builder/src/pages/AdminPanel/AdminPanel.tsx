import { Link } from 'react-router-dom'
import './AdminPanel.css'

export default function AdminPanel() {
  return (
    <div className="admin-panel-page">
      <h1>Admin Panel</h1>

      <div className="admin-links">
        <Link to="/admin-panel/database-builder" className="admin-link">Database Builder</Link>
      </div>

      <p>This page is intended as a local-only admin panel example (accessible on your machine at the loopback address).</p>
      <h2>Run commands</h2>
      <ul>
        <li><code>npm run dev:local</code> — start the dev server bound to 127.0.0.1:5000</li>
        <li><code>npm run build:local</code> — build into <code>dist-local/</code></li>
        <li><code>npm run preview:local</code> — preview the built site on 127.0.0.1:5000</li>
      </ul>
      <p>Note: The dev/preview servers are bound to localhost only. Built files in <code>dist-local/</code> can still be served externally if uploaded to a public host.</p>
    </div>
  )
}
