import { useCallback, useEffect, useRef, useState } from "react";

import { toFriendlyMessage } from "@/services/api";

export interface AsyncState<T> {
  data: T | null;
  isLoading: boolean;
  error: string | null;
  reload: () => void;
  setData: (value: T | null) => void;
}

/**
 * Small fetch-state helper shared by the feature hooks.
 * Pass `enabled: false` to defer the request (e.g. until the user acts).
 */
export function useAsyncData<T>(
  loader: () => Promise<T>,
  deps: readonly unknown[],
  options: { enabled?: boolean; errorMessage?: string } = {},
): AsyncState<T> {
  const { enabled = true, errorMessage } = options;
  const [data, setData] = useState<T | null>(null);
  const [isLoading, setIsLoading] = useState(enabled);
  const [error, setError] = useState<string | null>(null);
  const [nonce, setNonce] = useState(0);
  const loaderRef = useRef(loader);
  loaderRef.current = loader;

  useEffect(() => {
    if (!enabled) {
      setIsLoading(false);
      return;
    }
    let cancelled = false;
    setIsLoading(true);
    setError(null);

    loaderRef
      .current()
      .then((result) => {
        if (!cancelled) setData(result);
      })
      .catch((err: unknown) => {
        if (!cancelled) setError(toFriendlyMessage(err, errorMessage));
      })
      .finally(() => {
        if (!cancelled) setIsLoading(false);
      });

    return () => {
      cancelled = true;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [enabled, nonce, ...deps]);

  const reload = useCallback(() => setNonce((value) => value + 1), []);

  return { data, isLoading, error, reload, setData };
}
