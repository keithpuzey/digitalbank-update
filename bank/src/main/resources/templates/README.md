
# Demo App (automation-ready)

This package contains a static HTML/CSS/JS demo app styled to closely match the provided mobile screenshots.

## Start locally
- Open `index.html` directly in a browser, or
- Serve the folder with a local static server.

## Loan calculation model
The loan page recalculates all displayed values when either of these inputs change:
- `#amountSlider` / `[data-testid="loan-slider"]`
- the term chips in `[data-testid="term-row"]`

### Formula used
Monthly repayment uses the standard amortization formula:

`M = P * (r / (1 - (1 + r)^(-n)))`

Where:
- `P` = principal (loan amount)
- `r` = monthly interest rate = APR / 12
- `n` = term in months
- APR is fixed at **6.2%** for this demo

### What to assert in tests
The page exposes stable selectors:
- `[data-testid="quote-amount"]`
- `[data-testid="monthly-payment"]`
- `[data-testid="total-repayable"]`
- `[data-testid="total-interest"]`
- `[data-testid="afford-monthly"]`
- `[data-testid="headroom-text"]`

A test can move the slider, select a term, calculate the expected value using the same formula, and assert the rendered text.

### Example expected values
For **£35,000** over **36 months** at **6.2% APR**:
- Monthly ≈ **£1,067.94**
- Total repayable ≈ **£38,445.87**
- Total interest ≈ **£3,445.87**

(Outputs may differ by a penny depending on rounding strategy in a test runner, so use the same formula and formatting as the page for exact matching.)
