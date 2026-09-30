import { useEffect, useRef, useState } from "react";
import { X, Loader2, AlertCircle, RotateCcw, MapPin, Calendar, Wallet, Sparkles } from "lucide-react";
import { Button } from "@/components/common/Button";
import { ChatMessage } from "./ChatMessage";
import { ChatInput } from "./ChatInput";
import { aiService } from "@/services/aiService";
import { toFriendlyMessage } from "@/services/api";
import { useAuth } from "@/hooks/useAuth";
import type { ChatMessage as ChatMessageType, Conversation } from "@/types/ai";

interface TrevvyProps {
  isOpen: boolean;
  onClose: () => void;
}

const SUGGESTED_PROMPTS = [
  {
    icon: MapPin,
    label: "Plan a 3-day trip to Delhi",
    gradient: "from-rose-500/10 to-orange-500/10",
    border: "hover:border-rose-400/50",
    iconColor: "text-rose-500",
  },
  {
    icon: Sparkles,
    label: "Find places to visit near India Gate",
    gradient: "from-violet-500/10 to-blue-500/10",
    border: "hover:border-violet-400/50",
    iconColor: "text-violet-500",
  },
  {
    icon: Wallet,
    label: "Create a budget itinerary for Jaipur",
    gradient: "from-emerald-500/10 to-teal-500/10",
    border: "hover:border-emerald-400/50",
    iconColor: "text-emerald-500",
  },
  {
    icon: Calendar,
    label: "What should I do in Goa this weekend?",
    gradient: "from-amber-500/10 to-yellow-500/10",
    border: "hover:border-amber-400/50",
    iconColor: "text-amber-500",
  },
];

export function Trevvy({ isOpen, onClose }: TrevvyProps) {
  const { isAuthenticated } = useAuth();

  const [conversation, setConversation] = useState<Conversation | null>(null);
  const [isInitializing, setIsInitializing] = useState(false);
  const [messages, setMessages] = useState<ChatMessageType[]>([]);
  const [isSending, setIsSending] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [suggestionsVisible, setSuggestionsVisible] = useState(false);
  const [userCoords, setUserCoords] = useState<{ latitude: number; longitude: number } | null>(null);

  const hasShownWelcome = useRef(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    try {
      const saved = sessionStorage.getItem("travelit_user_coords");
      if (saved) {
        setUserCoords(JSON.parse(saved));
      }
    } catch {}

    if (typeof navigator !== "undefined" && navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (pos) => {
          const coords = {
            latitude: Number(pos.coords.latitude.toFixed(6)),
            longitude: Number(pos.coords.longitude.toFixed(6)),
          };
          setUserCoords(coords);
          try {
            sessionStorage.setItem("travelit_user_coords", JSON.stringify(coords));
          } catch {}
        },
        () => {},
        { timeout: 5000, enableHighAccuracy: false }
      );
    }
  }, []);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, isSending]);

  useEffect(() => {
    if (!isOpen) return;
    if (isAuthenticated && !conversation) {
      void initAuthConversation();
    }
    const t = setTimeout(() => setSuggestionsVisible(true), 200);
    return () => clearTimeout(t);
  }, [isOpen, isAuthenticated]);

  useEffect(() => {
    if (messages.length === 0) {
      setSuggestionsVisible(false);
      const t = setTimeout(() => setSuggestionsVisible(true), 150);
      return () => clearTimeout(t);
    }
    return undefined;
  }, [messages.length]);

  const initAuthConversation = async () => {
    setIsInitializing(true);
    setError(null);
    try {
      const existing = await aiService.getConversations();
      if (existing && existing.length > 0) {
        const latest = existing[0]!;
        setConversation(latest ?? null);
        const history = await aiService.getMessages(latest.id);
        if (history.length > 0) {
          setMessages(history);
          hasShownWelcome.current = true;
        }
      } else {
        const conv = await aiService.createConversation({ title: "Trevvy Chat" });
        setConversation(conv);
      }
    } catch (err) {
      setError(toFriendlyMessage(err, "Failed to start conversation"));
    } finally {
      setIsInitializing(false);
    }
  };

  const handleNewChat = async () => {
    if (isSending) return;
    hasShownWelcome.current = false;
    setMessages([]);
    setError(null);
    if (isAuthenticated) {
      setIsInitializing(true);
      try {
        const conv = await aiService.createConversation({ title: "Trevvy Chat" });
        setConversation(conv);
      } catch (err) {
        setError(toFriendlyMessage(err, "Failed to start new conversation"));
      } finally {
        setIsInitializing(false);
      }
    }
  };

  const handleSend = async (content: string) => {
    const trimmed = content.trim();
    if (!trimmed || isSending) return;

    const historyPayload = messages.map((m) => ({
      role: m.role.toUpperCase(),
      content: m.content,
    }));

    const now = new Date();
    const hours = now.getHours();
    const timeOfDay =
      hours < 12 ? "morning" : hours < 17 ? "afternoon" : hours < 21 ? "evening" : "night";
    const localTime = now.toLocaleString("en-US", {
      weekday: "long",
      hour: "numeric",
      minute: "2-digit",
      hour12: true,
    });
    const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    const clientContext = {
      localTime,
      timeZone,
      timeOfDay,
      ...(userCoords ? { latitude: userCoords.latitude, longitude: userCoords.longitude } : {}),
    };

    const optimisticUser: ChatMessageType = {
      id: Date.now(),
      conversationId: conversation?.id ?? 0,
      role: "user",
      content: trimmed,
      createdAt: new Date().toISOString(),
    };
    setMessages((prev) => [...prev, optimisticUser]);
    setIsSending(true);
    setError(null);

    try {
      let assistantMessage: ChatMessageType;
      if (isAuthenticated && conversation) {
        assistantMessage = await aiService.sendMessage(conversation.id, {
          content: trimmed,
          history: historyPayload,
          clientContext,
        });
      } else {
        assistantMessage = await aiService.guestChat({
          content: trimmed,
          history: historyPayload,
          clientContext,
        });
      }
      setMessages((prev) => [...prev, assistantMessage]);
      hasShownWelcome.current = true;
    } catch (err) {
      setError(toFriendlyMessage(err, "Failed to send message"));
      setMessages((prev) => prev.filter((m) => m.id !== optimisticUser.id));
    } finally {
      setIsSending(false);
    }
  };

  if (!isOpen) return null;

  const isFirstOpen = messages.length === 0 && !hasShownWelcome.current;

  return (
    <div className="fixed inset-0 z-50 flex items-end justify-end p-4 sm:items-center sm:justify-center">
      <div
        className="absolute inset-0 animate-in fade-in bg-black/50 backdrop-blur-sm duration-300"
        onClick={onClose}
        aria-hidden
      />

      <div 
        className="relative flex h-[600px] max-h-[90vh] w-full max-w-2xl animate-in slide-in-from-bottom-8 fade-in zoom-in-95 flex-col border border-border bg-card shadow-2xl duration-300 sm:h-[680px]"
        style={{ borderRadius: "2rem" }}
      >
        <div 
          className="flex items-center justify-between border-b border-border bg-gradient-to-r from-card to-card/50 p-4 backdrop-blur-sm"
          style={{ borderTopLeftRadius: "2rem", borderTopRightRadius: "2rem" }}
        >
          <div className="flex items-center gap-3">
            <img src="/trevvy-arrow.png" alt="" aria-hidden className="size-8 animate-pulse object-contain" />
            <div>
              <span className="bg-gradient-to-r from-foreground to-primary bg-clip-text font-display text-lg font-semibold text-transparent">
                Trevvy AI
              </span>
              <span className="ml-2 hidden text-xs text-muted-foreground sm:inline">
                {isAuthenticated ? "Your AI Travel Companion" : "Ask me anything about travel"}
              </span>
            </div>
          </div>
          <div className="flex items-center gap-1.5">
            <button
              onClick={handleNewChat}
              disabled={isSending || isInitializing}
              className="flex items-center gap-1.5 rounded-xl px-2.5 py-1.5 text-xs font-medium text-muted-foreground transition-all hover:bg-secondary hover:text-foreground disabled:opacity-50"
              title="Start a new chat"
              aria-label="New Chat"
            >
              <RotateCcw className="size-3.5" />
              <span>New Chat</span>
            </button>
            <button
              onClick={onClose}
              className="rounded-xl p-2 transition-all duration-200 hover:scale-110 hover:bg-secondary hover:rotate-90"
              aria-label="Close Trevvy"
            >
              <X className="size-5" />
            </button>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto p-4">
          {isInitializing ? (
            <div className="flex h-full items-center justify-center">
              <Loader2 className="size-8 animate-spin text-primary" />
            </div>
          ) : (
            <div className="space-y-4">
              {messages.map((message) => (
                <ChatMessage key={message.id} message={message} />
              ))}

              {isFirstOpen && !isSending && (
                <div className="space-y-5">
                  <div 
                    className="animate-in fade-in slide-in-from-bottom-3 duration-500 bg-secondary px-4 py-3 text-sm text-secondary-foreground"
                    style={{ borderRadius: "1.25rem" }}
                  >
                    <p>
                      Hi! I'm <strong>Trevvy</strong>, your AI travel companion for Travel.it.
                      {!isAuthenticated && (
                        <span className="text-muted-foreground"> You're chatting as a guest — no login needed!</span>
                      )}
                    </p>
                    <p className="mt-1">Where would you like to go?</p>
                  </div>

                  <div>
                    <p className="mb-3 text-center text-xs font-semibold uppercase tracking-wider text-muted-foreground">
                      Try a suggestion
                    </p>
                    <div className="flex flex-col items-center gap-2.5">
                      {SUGGESTED_PROMPTS.map((prompt, index) => (
                        <button
                          key={prompt.label}
                          onClick={() => void handleSend(prompt.label)}
                          disabled={isSending}
                          style={{
                            transitionDelay: suggestionsVisible ? `${index * 80}ms` : "0ms",
                            borderRadius: "1.25rem",
                          }}
                          className={[
                            "group relative overflow-hidden border border-border bg-gradient-to-br p-3 text-left w-72",
                            "transition-all duration-300",
                            prompt.gradient,
                            prompt.border,
                            suggestionsVisible
                              ? "translate-y-0 opacity-100"
                              : "translate-y-4 opacity-0",
                            "hover:-translate-y-0.5 hover:shadow-lg hover:shadow-black/5",
                            "active:scale-[0.98]",
                            "disabled:cursor-not-allowed disabled:opacity-40",
                          ].join(" ")}
                        >
                          <div className="absolute inset-0 -translate-x-full bg-gradient-to-r from-transparent via-white/10 to-transparent transition-transform duration-700 group-hover:translate-x-full" />

                          <div className="flex items-start gap-2.5">
                            <span className={`mt-0.5 shrink-0 ${prompt.iconColor} transition-transform duration-300 group-hover:scale-110`}>
                              <prompt.icon className="size-4" aria-hidden />
                            </span>
                            <span className="text-xs font-medium leading-snug text-foreground">
                              {prompt.label}
                            </span>
                          </div>
                        </button>
                      ))}
                    </div>
                  </div>
                </div>
              )}

              {isSending && (
                <div className="flex gap-3 animate-in fade-in duration-200">
                  <div className="flex size-8 shrink-0 items-center justify-center rounded-full bg-accent text-accent-foreground">
                    <Loader2 className="size-4 animate-spin" />
                  </div>
                  <div 
                    className="bg-secondary px-4 py-2.5 text-sm text-secondary-foreground"
                    style={{ borderRadius: "1.25rem" }}
                  >
                    Trevvy is thinking...
                  </div>
                </div>
              )}

              <div ref={messagesEndRef} />
            </div>
          )}

          {error && (
            <div 
              className="mt-4 flex items-start gap-2 bg-destructive/10 p-3 text-sm text-destructive"
              style={{ borderRadius: "1rem" }}
            >
              <AlertCircle className="mt-0.5 size-4 shrink-0" />
              <span className="flex-1">{error}</span>
              <Button size="sm" variant="outline" onClick={() => setError(null)} className="ml-auto shrink-0">
                Dismiss
              </Button>
            </div>
          )}
        </div>

        <ChatInput
          onSend={handleSend}
          disabled={isSending || isInitializing}
          placeholder="Ask Trevvy anything about travel..."
        />
      </div>
    </div>
  );
}
