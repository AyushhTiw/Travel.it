import api from "./api";
import { ENDPOINTS } from "./endpoints";
import type {
  ChatMessage,
  Conversation,
  CreateConversationRequest,
  SendMessageRequest,
} from "@/types/ai";

export const aiService = {
  // ── Authenticated (persisted) conversation management ─────────────────────

  async createConversation(request: CreateConversationRequest): Promise<Conversation> {
    const { data } = await api.post<Conversation>(ENDPOINTS.AI.CONVERSATIONS, request);
    return data;
  },

  async getConversations(): Promise<Conversation[]> {
    const { data } = await api.get<Conversation[]>(ENDPOINTS.AI.CONVERSATIONS);
    return data;
  },

  async getMessages(conversationId: number): Promise<ChatMessage[]> {
    const { data } = await api.get<ChatMessage[]>(ENDPOINTS.AI.MESSAGES(conversationId));
    return data;
  },

  async sendMessage(conversationId: number, request: SendMessageRequest): Promise<ChatMessage> {
    const { data } = await api.post<ChatMessage>(ENDPOINTS.AI.MESSAGES(conversationId), request);
    return data;
  },

  // ── Guest / stateless chat (no auth, no DB persistence) ───────────────────
  // Uses POST /api/v1/ai/chat — open to everyone including guests.

  async guestChat(request: SendMessageRequest): Promise<ChatMessage> {
    const { data } = await api.post<ChatMessage>(ENDPOINTS.AI.GUEST_CHAT, request);
    return data;
  },
};
