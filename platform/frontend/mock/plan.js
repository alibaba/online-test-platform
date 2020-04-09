export default {
  'POST /api/acp/query': (req, res) => {
    res.json({
      result: 'query acp successfully',
    });
  },
  'POST /api/taishan/query': (req, res) => {
    res.json({
      result: 'query taishan successfully',
    });
  },
  'POST /api/acp/start': (req, res) => {
    res.json({
      result: 'start acp successfully',
    });
  },
  'POST /api/taishan/start': (req, res) => {
    res.json({
      result: 'start tainshan successfully',
    });
  },
};
