"use client"

import React, {
  createContext
} from "react";

/**
 * Global state
 */
// Type
type GlobalContent = {
  count: number
  incrementCounter:(c: number) => void
  decrementCounter:(c: number) => void
}

// Initial state
const initialState = {
  count: 0,
  incrementCounter: (c: number) => {},
  decrementCounter: (c: number) => {}
}

// Reducer
let reducer = (state: GlobalContent, action: any) => {
  switch (action.type) {
    case "INCREMENT":
      return { ...state, count: state.count + action.payload };
    case "DECREMENT":
      return { ...state, count: state.count - action.payload };
    default:
      return state;
  }
}

// Context
const GlobalStateContext = createContext<GlobalContent>(initialState);

// Provider
function GlobalStateProvider(props: React.PropsWithChildren<{}>) {
  // Global state
  const [state, dispatch] = React.useReducer(reducer, initialState);

  // Provide the store to children
  return (
    <GlobalStateContext.Provider value={{
      count: state.count,
      incrementCounter: (c: number) => dispatch({ type: "INCREMENT", payload: c }),
      decrementCounter: (c: number) => dispatch({ type: "DECREMENT", payload: c })
    }}>
      {props.children}
    </GlobalStateContext.Provider>
  );
}

// Export
export { GlobalStateContext, GlobalStateProvider };
