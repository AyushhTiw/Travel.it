export interface ChatMessage {
  id: number;
  conversationId: number;
  role: "user" | "assistant" | "USER" | "ASSISTANT" | string;
  content: string;
  createdAt: string;
}

export interface Conversation {
  id: number;
  userId: number;
  title: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateConversationRequest {
  title: string;
}

export interface HistoryItem {
  role: string;
  content: string;
}

export interface ClientContext {
  localTime?: string;
  timeZone?: string;
  timeOfDay?: string;
  currentLocation?: string;
  latitude?: number;
  longitude?: number;
}

export interface SendMessageRequest {
  content: string;
  history?: HistoryItem[];
  clientContext?: ClientContext;
}


