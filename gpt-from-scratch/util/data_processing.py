from datasets import load_dataset

dataset = load_dataset(
    "mischeiwiller/swiss-german-text",
    split="train"
)

texts = [
    row["text"]
    for row in dataset
    if row["text"] and len(row["text"]) > 30
]

with open("../out/input.txt", "w", encoding="utf-8") as f:
    f.write("\n\n".join(texts))

print(f"Saved {len(texts)} texts")
