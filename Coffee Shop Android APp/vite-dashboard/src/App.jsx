import { useState } from 'react'
import OrderManager from './components/OrderManager'
import ComboManager from './components/ComboManager'
import './App.css'

function App() {
  const [activeTab, setActiveTab] = useState('orders')

  return (
    <div className="App">
      <div className="tab-navigation">
        <button 
          className={`tab-btn ${activeTab === 'orders' ? 'active' : ''}`}
          onClick={() => setActiveTab('orders')}
        >
          📦 Đơn Hàng
        </button>
        <button 
          className={`tab-btn ${activeTab === 'combos' ? 'active' : ''}`}
          onClick={() => setActiveTab('combos')}
        >
          🎁 Combo
        </button>
      </div>

      <div className="tab-content">
        {activeTab === 'orders' && <OrderManager />}
        {activeTab === 'combos' && <ComboManager />}
      </div>
    </div>
  )
}

export default App