/**
 * This part intends to read line, do not touch!!
 *
 * **/
let chunks: string[] = [];

function getInput(): Promise<string> {
  return new Promise((resolve, reject) => {
    process.stdin.on('end', () => {
      reject(new Error('No input received.'));
    });

    if (chunks.length > 0) {
      resolve(chunks.shift() ?? "");
    } else {
      process.stdin.once('data', (chunk: Buffer) => {
        const res = chunk.toString().trim().split('\n');
        chunks = res;
        resolve(chunks.shift() ?? "");
      });
    }
  });
}

/**
 * END OF GET_INPUT
 */

async function main() {
  let size = await getInput();
  let numberOfPlayers = await getInput();

  let index = 0;
  while (true) {
    let unknown1 = await getInput();
    let unknown2 = await getInput();
    for (let i = 0; i < Number(size); ++i) {
      let line = await getInput();
    }

    console.log(index);
    index = (index + 1)%18;
  }
}
main();

