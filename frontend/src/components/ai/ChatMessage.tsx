import { Bot, User } from "lucide-react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import type { ChatMessage as ChatMessageType } from "@/types/ai";

interface ChatMessageProps {
  message: ChatMessageType;
}

export function ChatMessage({ message }: ChatMessageProps) {
  const isUser = message.role?.toLowerCase() === "user";

  return (
    <div
      className={`animate-in slide-in-from-bottom-4 fade-in duration-300 flex gap-3 ${
        isUser ? "flex-row-reverse" : "flex-row"
      }`}
    >
      {/* Avatar */}
      {isUser ? (
        <div className="flex size-8 shrink-0 items-center justify-center rounded-full bg-primary text-primary-foreground transition-transform hover:scale-110">
          <User className="size-4" />
        </div>
      ) : (
        /* Trevvy bot avatar — twinkling ring effect */
        <div className="relative shrink-0">
{/* Inner subtle ring */}
          <span className="absolute inset-0 rounded-full bg-accent/20 animate-pulse" style={{ animationDuration: "1.8s" }} />
          {/* Icon circle */}
          <div className="relative flex size-8 items-center justify-center rounded-full bg-accent text-accent-foreground transition-transform hover:scale-110">
            <Bot className="size-4" />
          </div>
        </div>
      )}

      <div
        className={`max-w-[85%] rounded-2xl px-4 py-3 transition-all hover:shadow-md break-words ${
          isUser
            ? "bg-primary text-primary-foreground"
            : "bg-secondary text-secondary-foreground"
        }`}
      >
        {isUser ? (
          <p className="whitespace-pre-wrap text-sm leading-relaxed">{message.content}</p>
        ) : (
          <div className="text-sm leading-relaxed space-y-2">
            <ReactMarkdown
              remarkPlugins={[remarkGfm]}
              components={{
                h1: ({ children }) => (
                  <h1 className="text-base font-bold text-foreground mt-3 mb-1.5 first:mt-0">
                    {children}
                  </h1>
                ),
                h2: ({ children }) => (
                  <h2 className="text-sm font-bold text-foreground mt-3 mb-1.5 first:mt-0">
                    {children}
                  </h2>
                ),
                h3: ({ children }) => (
                  <h3 className="text-sm font-semibold text-foreground mt-2 mb-1 first:mt-0">
                    {children}
                  </h3>
                ),
                p: ({ children }) => (
                  <p className="text-sm leading-relaxed my-1.5 text-secondary-foreground">
                    {children}
                  </p>
                ),
                ul: ({ children }) => (
                  <ul className="text-sm my-1.5 pl-5 list-disc space-y-1 text-secondary-foreground">
                    {children}
                  </ul>
                ),
                ol: ({ children }) => (
                  <ol className="text-sm my-1.5 pl-5 list-decimal space-y-1 text-secondary-foreground">
                    {children}
                  </ol>
                ),
                li: ({ children }) => (
                  <li className="text-sm leading-relaxed">{children}</li>
                ),
                strong: ({ children }) => (
                  <strong className="font-semibold text-foreground">{children}</strong>
                ),
                em: ({ children }) => <em className="italic">{children}</em>,
                hr: () => <hr className="my-2.5 border-border" />,
                a: ({ href, children }) => (
                  <a
                    href={href}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="underline text-primary hover:text-primary/80 font-medium"
                  >
                    {children}
                  </a>
                ),
                code: ({ children }) => (
                  <code className="rounded bg-background/60 px-1.5 py-0.5 text-xs font-mono text-foreground border border-border/50">
                    {children}
                  </code>
                ),
                table: ({ children }) => (
                  <div className="overflow-x-auto my-2 rounded-lg border border-border">
                    <table className="min-w-full text-xs divide-y divide-border">
                      {children}
                    </table>
                  </div>
                ),
                th: ({ children }) => (
                  <th className="px-3 py-2 font-semibold text-left bg-muted/50 text-foreground">
                    {children}
                  </th>
                ),
                td: ({ children }) => (
                  <td className="px-3 py-2 border-t border-border">{children}</td>
                ),
              }}
            >
              {message.content}
            </ReactMarkdown>
          </div>
        )}
      </div>
    </div>
  );
}

