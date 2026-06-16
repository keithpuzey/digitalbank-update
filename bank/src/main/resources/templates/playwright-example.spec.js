
import { test, expect } from '@playwright/test';

function monthlyPayment(principal, apr, termMonths) {
  const r = apr / 12;
  const m = principal * (r / (1 - Math.pow(1 + r, -termMonths)));
  return new Intl.NumberFormat('en-GB', {
    style: 'currency', currency: 'GBP', minimumFractionDigits: 2, maximumFractionDigits: 2
  }).format(m);
}

test('loan quote recalculates from slider + term', async ({ page }) => {
  await page.goto('http://127.0.0.1:5500/loan.html');

  await page.getByTestId('loan-slider').fill('50000');
  await page.locator('[data-term="48"]').click();

  const expectedMonthly = monthlyPayment(50000, 0.062, 48);
  await expect(page.getByTestId('quote-amount')).toHaveText('£50,000');
  await expect(page.getByTestId('monthly-payment')).toHaveText(expectedMonthly);
});
