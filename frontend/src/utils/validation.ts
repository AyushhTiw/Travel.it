export type ValidationErrors<T> = Partial<Record<keyof T, string>>;

const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;

export function isValidEmail(email: string): boolean {
  return EMAIL_PATTERN.test(email.trim());
}

export interface LoginFormValues {
  email: string;
  password: string;
}

export function validateLogin(values: LoginFormValues): ValidationErrors<LoginFormValues> {
  const errors: ValidationErrors<LoginFormValues> = {};
  if (!values.email.trim()) errors.email = "Email is required.";
  else if (!isValidEmail(values.email)) errors.email = "Enter a valid email address.";
  if (!values.password) errors.password = "Password is required.";
  return errors;
}

export interface SignupFormValues {
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
}

export function validateSignup(values: SignupFormValues): ValidationErrors<SignupFormValues> {
  const errors: ValidationErrors<SignupFormValues> = {};
  if (!values.name.trim()) errors.name = "Name is required.";
  if (!values.email.trim()) errors.email = "Email is required.";
  else if (!isValidEmail(values.email)) errors.email = "Enter a valid email address.";
  if (!values.password) errors.password = "Password is required.";
  else if (values.password.length < 8) errors.password = "Use at least 8 characters.";
  if (!values.confirmPassword) errors.confirmPassword = "Confirm your password.";
  else if (values.confirmPassword !== values.password)
    errors.confirmPassword = "Passwords do not match.";
  return errors;
}

export interface TripFormValues {
  title: string;
  destination: string;
  startDate: string;
  endDate: string;
  description: string;
}

export function validateTrip(values: TripFormValues): ValidationErrors<TripFormValues> {
  const errors: ValidationErrors<TripFormValues> = {};
  if (!values.title.trim()) errors.title = "Trip title is required.";
  if (!values.destination.trim()) errors.destination = "Destination is required.";
  if (!values.startDate) errors.startDate = "Start date is required.";
  if (!values.endDate) errors.endDate = "End date is required.";
  if (values.startDate && values.endDate && values.endDate < values.startDate) {
    errors.endDate = "End date cannot be before the start date.";
  }
  return errors;
}

export interface BudgetFormValues {
  totalAmount: string;
  currency: string;
}

export function validateBudget(values: BudgetFormValues): ValidationErrors<BudgetFormValues> {
  const errors: ValidationErrors<BudgetFormValues> = {};
  const amount = Number(values.totalAmount);
  if (!values.totalAmount.trim()) errors.totalAmount = "Amount is required.";
  else if (!Number.isFinite(amount) || amount <= 0)
    errors.totalAmount = "Amount must be a positive number.";
  if (!values.currency.trim()) errors.currency = "Currency is required.";
  return errors;
}

export interface BudgetItemFormValues {
  category: string;
  amount: string;
  description: string;
}

export function validateBudgetItem(
  values: BudgetItemFormValues,
): ValidationErrors<BudgetItemFormValues> {
  const errors: ValidationErrors<BudgetItemFormValues> = {};
  const amount = Number(values.amount);
  if (!values.category.trim()) errors.category = "Category is required.";
  if (!values.amount.trim()) errors.amount = "Amount is required.";
  else if (!Number.isFinite(amount) || amount <= 0)
    errors.amount = "Amount must be a positive number.";
  return errors;
}

export interface PlaceFormValues {
  name: string;
  city: string;
  country: string;
  latitude: string;
  longitude: string;
}

export function validatePlace(values: PlaceFormValues): ValidationErrors<PlaceFormValues> {
  const errors: ValidationErrors<PlaceFormValues> = {};
  const lat = Number(values.latitude);
  const lng = Number(values.longitude);
  if (!values.name.trim()) errors.name = "Name is required.";
  if (!values.city.trim()) errors.city = "City is required.";
  if (!values.country.trim()) errors.country = "Country is required.";
  if (!Number.isFinite(lat) || lat < -90 || lat > 90)
    errors.latitude = "Latitude must be between -90 and 90.";
  if (!Number.isFinite(lng) || lng < -180 || lng > 180)
    errors.longitude = "Longitude must be between -180 and 180.";
  return errors;
}

export function hasErrors<T>(errors: ValidationErrors<T>): boolean {
  return Object.values(errors).some(Boolean);
}
